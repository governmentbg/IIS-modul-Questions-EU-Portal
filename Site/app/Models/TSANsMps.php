<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_MP_id
 * @property int        $Cts_id
 * @property string     $A_ns_MP_person
 * @property Date       $A_ns_MP_date
 * @property string     $A_ns_MP_signature
 * @property string     $A_ns_B_Sity
 * @property string     $A_ns_MP_Email
 * @property string     $A_ns_MP_url
 * @property string     $A_ns_IO
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class TSANsMps extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'TS_A_ns_Mps';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_MP_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_MP_id', 'Cts_id', 'A_ns_MP_person', 'A_ns_MP_date', 'A_ns_MP_signature', 'A_ns_B_Sity', 'A_ns_MP_Email', 'A_ns_MP_url', 'A_ns_IO', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'A_ns_MP_id' => 'int', 'Cts_id' => 'int', 'A_ns_MP_person' => 'string', 'A_ns_MP_date' => 'date', 'A_ns_MP_signature' => 'string', 'A_ns_B_Sity' => 'string', 'A_ns_MP_Email' => 'string', 'A_ns_MP_url' => 'string', 'A_ns_IO' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_ns_MP_date', 'created_at', 'updated_at', 'deleted_at'
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

    public function eq_ts()
    {
        return $this->belongsTo(CThemeSite::class, 'Cts_id');
    }

    public function eq_mp_lng()
    {
        return $this->hasMany(TSANsMpsLng::class, 'A_ns_MP_id');
    }

    public function eq_mp_doc()
    {
        return $this->hasMany(TSANsMpsDoc::class, 'A_ns_MP_id');
    }

    public function eq_mp_imp()
    {
        return $this->hasMany(TSANsMpImporter::class, 'A_ns_MP_id');
    }
    public function eq_mp_imp_c()
    {
        return $this->hasMany(TSANsMpImporterC::class, 'A_ns_MP_id');
    }
}
