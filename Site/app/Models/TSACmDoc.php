<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_Doc_id
 * @property int        $Cts_id
 * @property int        $A_ns_C_id
 * @property int        $A_Cm_DocT_id
 * @property Date       $A_Cm_Doc_date
 * @property string     $A_Cm_Doc_name
 * @property string     $A_Cm_Doc_file
 * @property int        $C_Lang_id
 * @property int        $A_Cm_Doc_order
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class TSACmDoc extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'TS_A_Cm_Doc';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_Cm_Doc_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_Doc_id', 'Cts_id', 'A_ns_C_id', 'A_Cm_DocT_id', 'A_Cm_Doc_date', 'A_Cm_Doc_name', 'A_Cm_Doc_file', 'C_Lang_id', 'A_Cm_Doc_order', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'A_Cm_Doc_id' => 'int', 'Cts_id' => 'int', 'A_ns_C_id' => 'int', 'A_Cm_DocT_id' => 'int', 'A_Cm_Doc_date' => 'date', 'A_Cm_Doc_name' => 'string', 'A_Cm_Doc_file' => 'string', 'C_Lang_id' => 'int', 'A_Cm_Doc_order' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_Cm_Doc_date', 'created_at', 'updated_at', 'deleted_at'
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

    public function eq_doc_t()
    {
        return $this->belongsTo(TSACmDocType::class, 'A_Cm_DocT_id');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
