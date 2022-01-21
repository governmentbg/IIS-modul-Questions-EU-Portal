<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Pl_ProgPL_id
 * @property int        $C_Lang_id
 * @property int        $Pl_ProgP_id
 * @property string     $Name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class PlProgramPointsLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Pl_Program_Points_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Pl_ProgPL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Pl_ProgPL_id', 'C_Lang_id', 'Pl_ProgP_id', 'Name', 'created_at', 'updated_at', 'deleted_at'
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
        'Pl_ProgPL_id' => 'int', 'C_Lang_id' => 'int', 'Pl_ProgP_id' => 'int', 'Name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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
    public function eq_pr_points()
    {
        return $this->belongsTo(PlProgramPoints::class, 'Pl_ProgP_id');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
