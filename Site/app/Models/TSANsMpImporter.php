<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_MPI_id
 * @property int        $A_ns_MP_id
 * @property int        $A_ns_MPD_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class TSANsMpImporter extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'TS_A_ns_Mp_Importer';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_MPI_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_MPI_id', 'A_ns_MP_id', 'A_ns_MPD_id', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'A_ns_MPI_id' => 'int', 'A_ns_MP_id' => 'int', 'A_ns_MPD_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_mp()
    {
        return $this->belongsTo(TSANsMps::class, 'A_ns_MP_id');
    }

    public function eq_mp_d()
    {
        return $this->belongsTo(ANsMps::class, 'A_ns_MPD_id', 'A_ns_MP_id');
    }
}
